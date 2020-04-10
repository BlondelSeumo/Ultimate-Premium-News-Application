<?php

namespace Liip\ImagineBundle\DependencyInjection\Factory\Resolver;

use Symfony\Component\Config\Definition\Builder\ArrayNodeDefinition;
use Symfony\Component\DependencyInjection\ContainerBuilder;
use Symfony\Component\DependencyInjection\DefinitionDecorator;
use Symfony\Component\DependencyInjection\Reference;

class FlysystemResolverFactory implements ResolverFactoryInterface
{
    /**
     * {@inheritdoc}
     */
    public function create(ContainerBuilder $container, $resolverName, array $config)
    {
        $resolverDefinition = new DefinitionDecorator('liip_imagine.cache.resolver.prototype.flysystem');
        $resolverDefinition->replaceArgument(0, new Reference($config['filesystem_service']));
        $resolverDefinition->replaceArgument(2, $config['root_url']);
        $resolverDefinition->replaceArgument(3, $config['cache_prefix']);
        $resolverDefinition->addTag('liip_imagine.cache.resolver', array(
            'resolver' => $resolverName,
        ));
        $resolverId = 'liip_imagine.cache.resolver.'.$resolverName;

        $container->setDefinition($resolverId, $resolverDefinition);

        return $resolverId;
    }

    /**
     * {@inheritdoc}
     */
    public function getName()
    {
        return 'flysystem';
    }

    /**
     * {@inheritdoc}
     */
    public function addConfiguration(ArrayNodeDefinition $builder)
    {
        $builder
            ->children()
                ->scalarNode('filesystem_service')->isRequired()->cannotBeEmpty()->end()
                ->scalarNode('cache_prefix')->defaultValue(null)->end()
                ->scalarNode('root_url')->isRequired()->cannotBeEmpty()->end()
            ->end()
        ;
    }
}
